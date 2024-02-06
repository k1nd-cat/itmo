import ACTION_CHANGE_Y from '../actions/change_y';

function action_change_y(value) {
	return {
		type: ACTION_CHANGE_Y,
		y: value
	};
}

export default action_change_y;