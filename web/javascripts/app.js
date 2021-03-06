angular.module('omni',['ui.router'])

.config(function($stateProvider,$urlRouterProvider,$httpProvider) {
	$urlRouterProvider.otherwise('/student_login');

	$stateProvider
		.state('home',{
			url:'/home',
			templateUrl:'/templates/home.html',
			controller:'HomeController'
		})
		.state('student',{
			url:'/student',
			templateUrl:'/templates/student.html'
		})
		.state('student.dashboard',{
			url:'/dash',
			templateUrl:'/templates/student.dashboard.html'
		})
		.state('student.profile',{
			url:'/profile',
			templateUrl:'/templates/student.prof.html'
		})
		.state('student.subject',{
			url:'/subject',
			templateUrl:'/templates/student.sub.html'
		})
		.state('student_appraisal_detail', {
			url:'/appraisal',
			templateUrl:'/templates/appraisal_detail.html',
			controller:'AppraisalController'
		})
		.state('student_appraisal_login',{
			url:'/student_login',
			templateUrl:'/templates/appraisal_index.html',
			controller:'StudentLoginController'
		})
		.state('staff_appraisal_login', {
			url:'/staff_login',
			templateUrl:'/templates/appraisal_login.html',
			controller:'StaffLoginController'
		})
		.state('staff_appraisal_dashboard', {
			url:'/staff_dashboard',
			templateUrl:'/templates/appraisal_staff_dashboard.html',
            controller:'StaffDashboardController'
		})
		.state('staff_report',{
			url:'/staff_report',
			templateUrl:'/templates/staff_report.html',
            controller : 'StaffReport'
		})
		.state('staff_csv',{
			url:'/staff_csv',
			templateUrl: '/templates/appraisal_staff_csv.html',
			controller: 'StaffCsvController'
		})
		.state('staff_flag_admin', {
			url:'/report_admin',
			templateUrl: '/templates/appraisal_report_admin.html',
			controller: 'StaffFlagController'
		})
		.state('staff_change_password',{
			url:'/change_password',
			templateUrl: '/templates/appraisal_change_password.html',
			controller: 'StaffChangePasswordController'
		})
		;

		$httpProvider.interceptors.push('authInterceptor');
});	
