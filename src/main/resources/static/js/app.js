var app = angular.module('angularboot', [ 'ngRoute']);

app.config(function($routeProvider,$locationProvider) {

    console.log("inside of app.js ...");

    $routeProvider
        .when('/', {
            templateUrl : '/template/home.html',
            controller : 'homeController'
        })

        .when('/list-all-users', {
            templateUrl : '/template/listuser.html',
            controller : 'listUserController'
        })

        .when('/list-all-roles', {
            templateUrl : '/template/listrole.html',
            controller : 'listRoleController'
        })

        .when('/list-all-tabs', {
            templateUrl : '/template/listTab.html',
            controller : 'listTabController'
        })

        .when('/createUser',{
            templateUrl : '/template/userregistration.html',
            controller : 'registerUserController'
        })

        .when('/createRole',{
            templateUrl : '/template/roleregistration.html',
            controller : 'registerRoleController'
        })

        .when('/update-user/:id',{
            templateUrl : '/template/listuser.html' ,
            controller : 'usersDetailsController'
        })

        .when('/login',{
            templateUrl : '/login/login.html',
            controller : 'loginController'
        })

        .when('/logout',{
            templateUrl : '/login/login.html',
            controller : 'logoutController'
        })

        .otherwise({
            redirectTo : '/login'
        });
});

app.config(['$httpProvider', function($httpProvider) {

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

}]);