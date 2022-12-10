package learn.capstone.data;

import learn.capstone.models.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
    public class AppUserJdbcTemplateRepository {

        private final JdbcTemplate jdbcTemplate;

        private final RowMapper<AppUser> userMapper = (ResultSet rs, int rowIndex) -> {
            AppUser user = new AppUser();
            user.setAppUserId(rs.getInt("app_user_id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password_hash"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setPrivateProfile(rs.getBoolean("private_profile"));
            return user;
        };

        private final RowMapper<GrantedAuthority> authorityMapper = (ResultSet rs, int index) ->
                new SimpleGrantedAuthority(rs.getString("name"));

        public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        public List<AppUser> findAll() {
            String sql = "select app_user_id, username, password_hash, enabled, private_profile "
                    + "from app_user ";
            return jdbcTemplate.query(sql, userMapper);
        }

        public Map<Integer, AppUser> getUserIds() {
            List<AppUser> all = findAll();
            return all.stream()
                    .collect(Collectors.toMap(AppUser::getAppUserId, user -> user));
        }
        public List<String> findAllRoles() {
            return jdbcTemplate.query("select * from app_role;",
                                      (rs, i) -> rs.getString("name"));
        }

        @Transactional
        public AppUser findByUsername(String username) {
            AppUser user = jdbcTemplate.query(
                            "select * from app_user where username = ?;",
                            userMapper,
                            username).stream()
                    .findFirst()
                    .orElse(null);

            if (user != null) {
                var authorities = getAuthorities(user.getAppUserId());
                user.setAuthorityNames(authorities);
            }

            return user;
        }

        @Transactional
        public AppUser findByAppUserId(int id) {
            AppUser user = jdbcTemplate.query(
                            "select * from app_user where app_user_id = ?;",
                            userMapper,
                            id).stream()
                    .findFirst()
                    .orElse(null);

            if (user != null) {
                var authorities = getAuthorities(user.getAppUserId());
                user.setAuthorityNames(authorities);
            }

            return user;
        }

        public AppUser add(AppUser user) {

            final String sql = "insert into app_user (username, password_hash) values (?,?);";
            final String sql2 = "insert into app_user_role (app_user_id, app_role_id) values (?,1);";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            int rowsAffected = jdbcTemplate.update(conn -> {
                PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                return statement;
            }, keyHolder);

            if (rowsAffected <= 0) {
                return null;
            }
            user.setAppUserId(Objects.requireNonNull(keyHolder.getKey()).intValue());

            rowsAffected = jdbcTemplate.update(conn -> {
                PreparedStatement statement = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, user.getAppUserId());
                return statement;
            }, keyHolder);

            return user;
        }

        @Transactional
        public boolean update(AppUser user) {

            String sql = "update app_user set "
                    + "username = ?, "
                    + "enabled = ? "
                    + "where app_user_id = ?;";

            int rowsAffected = jdbcTemplate.update(sql,
                                                   user.getUsername(),
                                                   user.isEnabled(),
                                                   user.getAppUserId());

            if (rowsAffected > 0) {
                setAuthorities(user);
                return true;
            }

            return false;
        }

        @Transactional
        public boolean deleteById(int appUserId) {
            String sql = "set sql_safe_updates = 0; "
                    + "delete from review where app_user_id = ?; "
                    + "delete from app_user_role where app_user_id = ?; "
                    + "delete from app_user where app_user_id = ?; "
                    + "set sql safe_updates = 1; ";

            int rowsAffected = jdbcTemplate.update(sql, appUserId, appUserId, appUserId);

            return rowsAffected > 0;
        }

    public boolean changePrivacy(AppUser user) {

        String sql = "update app_user set "
                + "private_profile = ? "
                + "where app_user_id = ?;";

        int rowsAffected = jdbcTemplate.update(sql,
                                               user.isPrivateProfile(),
                                               user.getAppUserId());

        return rowsAffected > 0;
    }

        public boolean changePassword(AppUser user) {

            String sql = "update app_user set "
                    + "password_hash = ? "
                    + "where app_user_id = ?;";

            int rowsAffected = jdbcTemplate.update(sql,
                                                   user.getPassword(),
                                                   user.getAppUserId());

            return rowsAffected > 0;
        }

        private void setAuthorities(AppUser user) {

            jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getAppUserId());

            for (var name : user.getAuthorityNames()) {
                String sql = "insert into app_user_role (app_user_id, app_role_id) "
                        + "values (?, (select app_role_id from app_role where name = ?));";
                jdbcTemplate.update(sql, user.getAppUserId(), name);
            }
        }

        private List<String> getAuthorities(int appUserId) {

            String sql = "select r.app_role_id, r.name "
                    + "from app_user_role aur "
                    + "inner join app_role r on aur.app_role_id = r.app_role_id "
                    + "where aur.app_user_id = ?";

            return jdbcTemplate.query(sql,
                                      (rs, i) -> rs.getString("name"),
                                      appUserId);
        }
    }