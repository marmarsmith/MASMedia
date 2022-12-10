import React, { useState } from "react";
import SearchIcon from '@mui/icons-material/Search';
import CloseIcon from '@mui/icons-material/Close';
import "./SearchBar.css";
import { Link } from "react-router-dom";

function SearchBar({placeholder, data}) {
    console.log(typeof(data));
    console.log(data);

    console.log("Hello Marcel");

    const [filteredData, setFilteredData] = useState([]);
    const [wordEntered, setWordEntered] = useState("");

    const handleFilter = (event) => {
        const searchWord = event.target.value;
        setWordEntered(searchWord);
        const newFilter = data.filter((value) => {
          return value.title.toLowerCase().includes(searchWord.toLowerCase());
        });
    
        if (searchWord === "") {
          setFilteredData([]);
        } else {
          setFilteredData(newFilter);
        }
      };
    
      const clearInput = () => {
        setFilteredData([]);
        setWordEntered("");
      };

      return (
        <div className="search">
          <div className="searchInputs">
            <input
              type="text"
              placeholder={placeholder}
              value={wordEntered}
              onChange={handleFilter}
            />
            <div className="searchIcon">
              {filteredData.length === 0 ? (
                <SearchIcon />
              ) : (
                <CloseIcon id="clearBtn" onClick={clearInput} />
              )}
            </div>
          </div>
          {filteredData.length !== 0 && (
            <div className="mediaResult">
              {filteredData.slice(0, 15).map((value, key) => {
                return (
                  <Link to={`/review/${value.mediaId}`} className="mediaItem">
                    <p>{value.title} </p>
                    </Link>

                );
              })}
            </div>
          )}
        </div>
      );
    }

export default SearchBar;