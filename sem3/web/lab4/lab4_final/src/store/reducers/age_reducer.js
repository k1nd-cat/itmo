import ACTION_CHANGE_AGE from '../actions/change_age';
import initialState from '../initialState';

export default function value_2(state = initialState.age, action) {
    switch(action.type) {
        case ACTION_CHANGE_AGE: return action.age;
        
        default: return state;
    }
}