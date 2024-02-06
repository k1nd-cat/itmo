import ACTION_CHANGE_AGE from '../actions/change_age';

function action_change_age(value) {
	return {
		type: ACTION_CHANGE_AGE,
		age: value
	};
}

export default action_change_age;