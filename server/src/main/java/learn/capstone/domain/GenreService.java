package learn.capstone.domain;

import learn.capstone.data.GenreRepository;
import learn.capstone.data.MediaRepository;
import learn.capstone.models.Genre;
import learn.capstone.models.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final GenreRepository repository;
    private final MediaRepository mediaRepository;

    public GenreService(GenreRepository repository, MediaRepository mediaRepository){
        this.repository = repository;
        this.mediaRepository = mediaRepository;
    }


    public List<Genre> findAll(){
        return repository.findAll();
    }

    public Genre findById(int genreId) {
        return repository.findById(genreId).orElse(null);
    }

//    public List<Genre> findByMedia(List<Media> mediaList){
//        List<Genre> allGenreThatMeetsMedia = new ArrayList<>();
//        List<Media> mediaListTwo = mediaRepository.findByMediaIdIn(mediaList.stream().map(m -> m.getMediaId()).toList());
//        return mediaListTwo.stream().flatMap(m -> m.getGenres().stream()).distinct().toList();
//    }//findByGenre

    public Result<Genre> add(Genre genre) {
        Result<Genre> result = validate(genre);
        List<Genre> genreList = repository.findAll();
        if (!result.isSuccess()) {
            return result;
        }

        if (genre != null && genre.getGenreId() != 0) {
            result.addMessage("Genre Id cannot be set for `add` operation.", ResultType.INVALID);
            return result;
        }
        if (result.isSuccess()) {
            genreList.add(genre);
            repository.save(genre);
            result.setPayload(genre);
        }
        return result;
    }

    public Result<Genre> update(Genre genre) {
        Result<Genre> result = validate(genre);
        if(!result.isSuccess()){
            return result;
        }

        if (genre.getGenreId() <= 0) {
            result.addMessage("Genre Id must be set for update operation", ResultType.INVALID);
            return result;
        }

        if(!repository.existsById(genre.getGenreId())){
            String msg = String.format("Genre Id: %s, not found", genre.getGenreId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        if (result.isSuccess()) {
            Genre thingToUpdate = repository.findById(genre.getGenreId()).orElse(null);

            assert thingToUpdate != null;
            thingToUpdate.setName(genre.getName());


            genre = repository.save(thingToUpdate);
            result.setPayload(genre);

        }
        return result;
    }//update

    public Result<Genre> deleteById(int genreId) {
        Result<Genre> result = new Result();
        if (!repository.existsById(genreId)) {
            String msg = String.format("Genre Id: %s is not found.", genreId);
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }

        if (result.isSuccess()) {
            repository.deleteById(genreId);
        }

        return result;
    }

    private Result validate(Genre genre) {
        Result result = new Result();
        if (genre == null) {
            result.addMessage("Genre Cannot be Null.", ResultType.INVALID);
            return result;
        }
        if (Validations.isNullOrBlank(genre.getName())) {
            result.addMessage("Name is required.", ResultType.INVALID);
        }

        if(result.isSuccess()){
            List<Genre> existingGenres = repository.findAll();

            for(Genre existingGenre : existingGenres){

                if(existingGenre.getGenreId() != genre.getGenreId() &&
                        existingGenre.getName().equalsIgnoreCase(genre.getName())){
                    result.addMessage(genre.getName() + " is already a genre.", ResultType.INVALID);
                }
            }
        }
        return result;
    }


}


