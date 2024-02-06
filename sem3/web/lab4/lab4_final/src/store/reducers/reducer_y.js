import ACTION_CHANGE_Y from '../actions/change_y';
import initialState from '../initialState';

export default function value_y(state = initialState.y, action) {
    switch(action.type) {
        case ACTION_CHANGE_Y: return action.y;
        
        default: return state;
    }
}