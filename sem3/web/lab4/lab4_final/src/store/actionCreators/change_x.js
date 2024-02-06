import ACTION_CHANGE_X from '../actions/change_x';

function action_change_x(value) {
	return {
		type: ACTION_CHANGE_X,
		x: value
	};
}

export default action_change_x;