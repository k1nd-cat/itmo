import ACTION_CHANGE_SAVED from '../actions/change_saved';
import initialState from '../initialState';

export default function value_saved(state = initialState.saved, action) {
    switch(action.type) {
        case ACTION_CHANGE_SAVED: return action.saved;
        
        default: return state;
    }
}