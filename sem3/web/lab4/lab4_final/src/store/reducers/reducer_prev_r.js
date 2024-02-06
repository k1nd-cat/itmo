import ACTION_CHANGE_PREV_R from '../actions/change_prev_r';
import initialState from '../initialState';

export default function value_prev_r(state = initialState.prev_r, action) {
    switch(action.type) {
        case ACTION_CHANGE_PREV_R: return action.prev_r;
        
        default: return state;
    }
}