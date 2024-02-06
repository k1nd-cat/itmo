import ACTION_CHANGE_R from '../actions/change_r';
import initialState from '../initialState';

export default function value_r(state = initialState.r, action) {
    switch(action.type) {
        case ACTION_CHANGE_R: return action.r;
        
        default: return state;
    }
}