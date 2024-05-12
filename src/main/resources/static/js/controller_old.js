/**
 * Created by farshidkhalaj on 8/25/20.
 */
app.controller('homeController', function($rootScope, $scope,$http, $location, $route){

    console.log("inside of home controller...");

    if ($rootScope.authenticated) {
        $location.path("/");
        $scope.loginerror = false;
    } else {
        $location.path("/login");
        $scope.loginerror = true;
    }
});

app.controller ('loginController', function($rootScope, $scope, $http, $location, $route) {

    console.log("inside of login controller...");

    if ($rootScope.authenticated) {

        console.log("rootScope.authenticated is true in loginController...");

        $location.path("/");
        callback && callback();
    }

    console.log("authenticated variable is false in loginController...");

    $scope.credentials = {};
    $scope.resetForm = function () {
        $scope.credentials = null;
    }
    var authenticate = function (credentials, callback) {

        console.log("Username : " + $scope.credentials.username + "  Password : " + $scope.credentials.password);

        var headers = $scope.credentials ? {
            authorization: "Basic "
            + btoa($scope.credentials.username + ":"
                + $scope.credentials.password)
        } : {};

        console.log("Header which is passed is :" + JSON.stringify(headers));

        $http.get('user', {

            headers: headers

        }).then(function (response) {

            if (response.data.name) {

                $rootScope.authenticated = true;

                console.log("log inside of authenticate method...");
                console.log(JSON.stringify(response.data));

                console.log("finalFeatures items...");
                $rootScope.finalfeatures = response.data.principal.finalFeatures;
                console.log(JSON.stringify($rootScope.finalfeatures));

                console.log("finalTabs items...");
                $rootScope.finaltabs = response.data.principal.authorities;
                console.log(JSON.stringify($rootScope.finaltabs));

                $rootScope.final_feature_name = [];
                $rootScope.final_role_name = [];

                for (var ki in $rootScope.finalfeatures) {

                    $rootScope.final_feature_name.push($rootScope.finalfeatures[ki].featurename);
                    console.log(JSON.stringify($rootScope.final_feature_name));
                    
                }


            } else {
                $rootScope.authenticated = false;
            }
            callback && callback();
        }, function () {
            $rootScope.authenticated = false;
            callback && callback();
        });
    }

    authenticate();

    $scope.loginUser = function() {

        console.log("inside of loginUser function ...")

        authenticate($scope.credentials, function () {

            console.log("After Authentication Call...");

            if ($rootScope.authenticated) {

                console.log("Authentication done successfully...");

                $location.path("/");
                $scope.loginerror = false;
            }
            else {
                $location.path("/login");
                console.log("Authentication was not successfull...");
                $scope.loginerror = true;
            }
        });
    };

});

app.controller('logoutController', function($rootScope, $scope, $http, $location, $route){

    console.log("inside of logout controller...");

    $http.post('logout', {}).finally(function() {

        console.log("finally block executed...");

        $rootScope.authenticated = false;
        $location.path("/");

    });

});


app.controller('listUserController', function($rootScope,$scope, $http, $location,$routeParams,$route) {

        console.log("Inside of listUserController...");

        if ($rootScope.authenticated && $rootScope.final_feature_name.includes('User Managment')) {

            $scope.roless = [{"id": 1, "name": "USER"}, {"id": 2, "name": "ADMIN"}];

            $scope.userId = $routeParams.id;

            console.log(JSON.stringify("user id has passed as = " + $scope.userId));

            $http({
                method: 'GET',
                url: 'http://localhost:8080/api/user/'
            }).then(function (response) {

                console.log(JSON.stringify(response.data));
                $rootScope.users = response.data;

            });


            $scope.submitUserForm = function () {

                console.log(JSON.stringify("Create User Controller..."));

                $http({
                    method: 'POST',
                    url: 'http://localhost:8080/api/user/',
                    data: $scope.user,
                }).then(function (response) {
                    $('.modal-backdrop').hide();
                    $location.path("/list-all-users");
                    $route.reload();
                }, function (errResponse) {
                    $scope.errorMessage = errResponse.data.errorMessage;
                });
            }

            $scope.resetForm = function () {
                $scope.user = null;
            };

            $scope.editUser = function(userId) {
                $location.path("/update-user/" + userId);
            }

            $scope.deleteUser = function(userId) {
                $http({
                    method : 'DELETE',
                    url : 'http://localhost:8080/api/user/' + userId
                })
                    .then(
                        function(response) {
                            $location.path("/list-all-users");
                            $route.reload();
                        });
            }
        }

        else {
            $location.path("/login");
            $scope.loginerror = true;
        }

    $rootScope.$on("getAllUsers", function () {

        if ($rootScope.authenticated && $rootScope.final_feature_name.includes('User Managment')) {

            $http({
                method: 'GET',
                url: 'http://localhost:8080/api/user/'
            }).then(function (response) {

                console.log(JSON.stringify(response.data));
                $rootScope.users = response.data;

            });
        }

    });

});

app.controller('usersDetailsController',function($rootScope,$scope, $http, $location, $routeParams,$route) {

    if ($rootScope.authenticated && $rootScope.final_feature_name.includes('User Managment')) {

        $scope.roless = [{"id": 1, "name": "USER"}, {"id": 2, "name": "ADMIN"}];

        $scope.userId = $routeParams.id;

        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/user/' + $scope.userId
        }).then(function (response) {

            $scope.user = response.data;
            $scope.selectedItemvalue = $scope.user.roles.id;
            console.log("CurrentRole..." + $scope.selectedItemvalue);
            console.log("Current data of user..." + JSON.stringify(response.data));
            $rootScope.$emit("getAllUsers");
        });

        $scope.submitUserForm = function (userId) {

                console.log("Inside of submitUserForm else part...");

                $scope.user.roles.id  = $scope.selectedItemvalue;

                $http({
                    method: 'PUT',
                    url: 'http://localhost:8080/api/user/' + userId,
                    data: $scope.user
                }).then(
                    function (response) {

                        console.log("Updated Role data..."+ JSON.stringify(response.data));
                        $('.modal-backdrop').hide();
                        $location.path("/list-all-users");
                        $route.reload();
                    },
                    function (errResponse) {
                        $scope.errorMessage = "Error while updating User - Error Message: '" + errResponse.data.errorMessage;
                    });
            }

        }
});

/*app.controller('usersDetailsController',function($rootScope,$scope, $http, $location, $routeParams,$route) {

        if ($rootScope.authenticated && $rootScope.final_feature_name.includes('User Managment')) {

            $scope.roless = [{"id": 1, "name": "USER"}, {"id": 2, "name": "ADMIN"}];

            $scope.userId = $routeParams.id;

            $http({
                method: 'GET',
                url: 'http://localhost:8080/api/user/' + $scope.userId
            }).then(function (response) {

                $scope.user = response.data;
                $scope.selectedItemvalue = $scope.user.roles.id;
                console.log("CurrentRole..."+ $scope.selectedItemvalue);
                console.log("Current data of user..."+ JSON.stringify(response.data));
            });

            $scope.updateUserForm = function (userId) {

                $scope.user.roles.id  = $scope.selectedItemvalue;

                console.log("****role id after form submition 1 *** is : " +  $scope.user.roles.id);
                console.log("****role name after form submition 1 *** is : " +  $scope.user.roles.name);

                $http({
                    method: 'PUT',
                    url: 'http://localhost:8080/api/user/' + userId,
                    data: $scope.user
                }).then(
                    function (response) {

                        console.log("Updated Role data..."+ JSON.stringify(response.data));
                        $location.path("/list-all-users");
                        $route.reload();
                    },
                    function (errResponse) {
                        $scope.errorMessage = "Error while updating User - Error Message: '" + errResponse.data.errorMessage;
                    });
            }
            $scope.resetForm = function () {
                $scope.user = null;
            };
        }
        else{
            $location.path("/login");
            $scope.loginerror = true;
        }
    }
);*/

