angular.module('omni')
.factory('AppraisalFactory', function  ($http, $window) {

	var _questions = [];

	var _teacherDetails = [];

	var getTeacherDetails = function  (usn) {
		// body...
		return $http.post('/JSONtest',{usn: usn})
	};

	var getQuestions = function  () {
		return $http.get('/get_appraisal_questions');
	};

    var sendFeedback = function (data) {
        return $http.post('/feedback', data);
    };

    var completeAppraisal = function (data) {
        return $http.post('/complete_appraisal', data);
    }

    var queue = false;

	/*var getTeacherDetails = function  (usn) {
		return _teacherDetails(usn);
	}*/

	/**
	mysql close connection
	totlaAppraisal check 
	two request for each
	21 29
	**/

	return {
		teacherDetails: function  () {
			return _teacherDetails;
		},

		getTeachers: function  (usn) {
	
		return getTeacherDetails(usn)	
			.then(function  (result) {
				console.log("Success here", result);
				_teacherDetails = result;
				return _teacherDetails.data;
			});
		},

		getQuestions: function  () {
			return getQuestions()
				.then(function  (result) {
					console.log("Questions ", result);
					_questions = result;
					return _questions.data;
				});
		},

        submit: sendFeedback,

        complete: completeAppraisal,

        validateSubmit: function (req) {
        	this.submit(req)
        		.then(function  (response) {
        			console.log('Done', JSON.parse($window.localStorage.pendingRequests).length)
        			if(response.data) {
        				var reqs = JSON.parse($window.localStorage.pendingRequests);
        				var i = reqs.indexOf(req);
        				reqs.splice(i, 1);
        				$window.localStorage.pendingRequests = JSON.stringify(reqs);
        			}
        		})
        },

        completePendingRequest: function  () {
        	console.log("complete pending reqe",$window.localStorage.pendingRequests);
        	var self = this;

        	if (this.queue) {
        		console.log('Processing queue')
        		return
        	} else {
        		this.queue = true;
	        	var reqs = $window.localStorage.pendingRequests;
	        	if (reqs) {
	        		reqs = JSON.parse(reqs);
	        		reqs.forEach(function  (req) {
	        			self.validateSubmit(req);
	        		})
	        	}
        	}
        },
        setQueue: function  (Processing) {
        	this.queue = Processing;
        },

        checkComplete: function  (data) {
        	return $http.get('/check_complete', data);
        }
	};
});