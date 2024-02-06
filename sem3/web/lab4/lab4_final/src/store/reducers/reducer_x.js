import ACTION_CHANGE_X from '../actions/change_x';
import initialState from '../initialState';

export default function value_y(state = initialState.x, action) {
    switch(action.type) {
        case ACTION_CHANGE_X: return action.x;
        
        default: return state;
    }
}