import ReactStars from 'react-stars'
import React from 'react'
import ReviewForm from '../ReviewForm';
import { useState } from 'react';

function Stars({stars, ratingChanged, edit}){




  return(
    <ReactStars
  count={5}
  onChange={ratingChanged}
  value={stars}
  size={24}
  color2={'#ffd700'}
  edit={edit} />
  )
  

};

export default Stars;