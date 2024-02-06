function mapStateToProps(component) {
	switch (component) {
		case "Input": {
			return function (state) {
				return {
					x: state.x,
					y: state.y,
					r: state.r,
				};
			}
		}
		case "Results": {
			return function (state) {
				return {
					saved: state.saved,
					prev_r: state.prev_r,
					r: state.r,
				};
			}
		}
		case "Canvas": {
			return function (state) {
				return {
					r: state.r,
				};
			}
		}
		default: return undefined;
	}
}

export default mapStateToProps;