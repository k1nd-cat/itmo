import { combineReducers } from 'redux';
import x from './reducer_x'; 
import y from './reducer_y'; 
import r from './reducer_r'; 
import prev_r from './reducer_prev_r'
import saved from './reducer_saved'; 

const reducers = combineReducers({
    x,
    y,
    r,
    prev_r,
    saved,
});

export default reducers;