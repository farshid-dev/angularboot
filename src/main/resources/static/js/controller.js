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

                $rootScope.authenticated = response.data.authenticated;

                console.log(JSON.stringify(response.data));
                //console.log(JSON.stringify(response.data.name));

                $rootScope.finalfeatures = response.data.principal.featuresMap;
                $rootScope.finaltabs = response.data.principal.authorities;

                $rootScope.navbarData = [];

                // Iterate over the tabs
                for (var tabName in $rootScope.finalfeatures) {

                    var tabFeatures = $rootScope.finalfeatures[tabName];

                    var tabObject = {
                        tabName: tabName,
                        features: []
                    };

                    for (var i = 0; i < tabFeatures.length; i++) {
                        tabObject.features.push(tabFeatures[i].featurename);
                        $rootScope.currentRole = tabFeatures[i].rolename;
                    }

                    $rootScope.navbarData.push(tabObject);
                }

                console.log(JSON.stringify($rootScope.navbarData));


                //Get the List of Roles
                $http({
                    method: 'GET',
                    url: 'http://localhost:8080/api/role/',
                    data: $scope.role,
                }).then(function (response) {

                    $rootScope.roleNames = [];
                    angular.forEach(response.data, function(item) {
                        $rootScope.roleNames.push({id: item.id, name: item.name});
                    });

                });
            }
             else {
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

app.controller('registerUserController', function($rootScope,$scope, $http, $location, $route) {

    if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

        //$scope.roless = [{"id": 1, "name": "USER"}, {"id": 2, "name": "ADMIN"}];
        $scope.roless = $rootScope.roleNames;

        console.log("inside of registerUserController...");

        $scope.submitUserForm = function () {

            console.log("inside of submitUserForm to register user...");

            $scope.user.userRoleId = $scope.user.userRoleId;

            console.log("Username  : " + $scope.user.username);
            console.log("User Role Id : " + $scope.user.userRoleId);

            $http({
                method: 'POST',
                url: 'http://localhost:8080/api/user/',
                data: $scope.user,
            }).then(function (response) {
                $location.path("/list-all-users");
                $route.reload();
            }, function (errResponse) {
                $scope.errorMessage = errResponse.data.errorMessage;
            });
        }
        $scope.resetForm = function () {
            $scope.user = null;
        };
    } else{
        $location.path("/login");
        $scope.loginerror = true;
    }
});

app.controller('registerRoleController', function($rootScope,$scope, $http, $location, $route) {

    if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

        console.log("inside of registerRoleController...");

        $scope.submitRoleForm = function () {

            console.log("inside of submitRoleForm of registerRoleController...");

            $http({
                method: 'POST',
                url: 'http://localhost:8080/api/role/',
                data: $scope.role,
            }).then(function (response) {
                $location.path("/list-all-roles");
                $route.reload();
            }, function (errResponse) {
                $scope.errorMessage = errResponse.data.errorMessage;
            });
        }
        $scope.resetForm = function () {
            $scope.role = null;
        };
    } else{
        $location.path("/login");
        $scope.loginerror = true;
    }
});

app.controller('listUserController', function($rootScope,$scope, $http, $location, $route) {

    console.log("Inside of listUserController...");

    if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

        console.log("Passed the condition inside the listUserController");

        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/user/'
        })
            .then(
                function (response) {

                    console.log(JSON.stringify(response.data));

                    $scope.users = response.data;

                });

        $scope.editUser = function (userId) {
            //$location.path("/update-user/" + userId);

            if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

                console.log("I am inside the editUser of listUserController to update the user");

                //$scope.roless = [{"id": 1, "name": "USER"}, {"id": 2, "name": "ADMIN"}];

                $scope.roless = $rootScope.roleNames;

                $scope.userId = userId;

                console.log("userId is : " + $scope.userId);

                $http({
                    method: 'GET',
                    url: 'http://localhost:8080/api/user/' + $scope.userId
                }).then(function (response) {

                    console.log("Current data of user to edit..." + JSON.stringify(response.data));
                    $scope.user = response.data;
                    $scope.selectedItemvalue = $scope.user.userRoleId;
                    console.log("CurrentRole..." + $scope.selectedItemvalue);
                    console.log("Current data of user..." + JSON.stringify(response.data));
                });
            } else {
                $location.path("/login");
                $scope.loginerror = true;
            }
        }

        //Below Function added here to support user registration button above data table
        $scope.registerUserForm = function () {

            console.log("inside of submitUserForm of listUserController...");
            console.log("User information to save : " + $scope.user);


            $scope.user.userRoleId = $scope.user.userRoleId;

            console.log("User Role to save  : " + $scope.user.userRoleId);

            $http({
                method: 'POST',
                url: 'http://localhost:8080/api/user/',
                data: $scope.user,
            }).then(function (response) {
                //$location.path("/list-all-users");
                $('.modal-backdrop').hide();
                $route.reload();
            }, function (errResponse) {
                $scope.errorMessage = errResponse.data.errorMessage;
            });
        }

        $scope.submitUserForm = function (userId) {

            console.log("Data To update Purpose : "+ JSON.stringify($scope.user));

            $http({
                method: 'PUT',
                url: 'http://localhost:8080/api/user/' + userId,
                data: $scope.user
            }).then(function (response) {

                    console.log("Updated Role data..."+ JSON.stringify(response.data));
                    $('.modal-backdrop').hide();
                    //$location.path("/list-all-users");
                    $route.reload();
                },
                function (errResponse) {
                    $scope.errorMessage = "Error while updating User - Error Message: '" + errResponse.data.errorMessage;
                });
        }

        $scope.deleteUser = function (userId) {
            $http({
                method: 'DELETE',
                url: 'http://localhost:8080/api/user/' + userId
            })
                .then(
                    function (response) {
                        $location.path("/list-all-users");
                        $route.reload();
                    });
        }

        $scope.clearUser = function() {
            $scope.user = {}; // Clear user object
            $scope.roless = $rootScope.roleNames;
        };

    }
});

app.controller('usersDetailsController',function($rootScope,$scope, $http, $location, $routeParams,$route) {

    if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

        console.log("I am inside the usersDetailsController to update the user");

        //$scope.roless = [{"id": 1, "name": "USER"}, {"id": 2, "name": "ADMIN"}];
        $scope.roless = $rootScope.roleNames;

        $scope.userId = $routeParams.id;
        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/user/' + $scope.userId
        }).then(function (response) {

            console.log("Current data of user to edit..."+ JSON.stringify(response.data));
            $scope.user = response.data;
            $scope.selectedItemvalue = $scope.user.userRole;
            console.log("CurrentRole..."+ $scope.selectedItemvalue);
            console.log("Current data of user..."+ JSON.stringify(response.data));
        });


        $scope.submitUserForm = function (userId) {

            console.log("****role id after form submition 1 *** is : " +  $scope.user.userRole);
            console.log("****role name after form submition 1 *** is : " +  $scope.user.username);

            $http({
                method: 'PUT',
                url: 'http://localhost:8080/api/user/' + userId,
                data: $scope.user
            }).then(function (response) {

                    console.log("Updated Role data..."+ JSON.stringify(response.data));
                    $('.modal-backdrop').hide();
                    $location.path("/list-all-users");
                    //$route.reload();
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
);


app.controller('listFeatureController', function($rootScope, $scope, $http, $location, $route)
{
     console.log("Inside of listFeatureController...");

    if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN")
    {
        console.log("Passed the condition inside the listFeatureController");

        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/features/'
        }).then(function(response) {
            console.log(JSON.stringify(response.data));
            $scope.features = response.data;
        });

        $scope.editFeature = function(featureId)
        {
            if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {
                console.log("I am inside the editFeature of listFeatureController to update the feature");

                $scope.featureId = featureId;

                $http({
                    method: 'GET',
                    url: 'http://localhost:8080/api/features/' + $scope.featureId
                }).then(function(response) {
                    console.log("Current data of feature to edit..." + JSON.stringify(response.data));
                    $scope.feature = response.data;

                    if ($scope.feature.readWriteOption === 1) {
                        $scope.feature.option = 'read_write';
                    } else if ($scope.feature.readOption === 1) {
                        $scope.feature.option = 'read';
                    } else {
                        $scope.feature.option = ''; // default, no option selected
                    }
                });
            } else{
                $location.path("/login");
                $scope.loginerror = true;
            }
        }

        $scope.registerFeatureForm = function()
        {
            console.log("inside of registerFeatureForm of listFeatureController...");

            $scope.feature.readOption = $scope.feature.readOption ? 1 : 0;
            $scope.feature.readWriteOption = $scope.feature.readWriteOption ? 1 : 0;

            console.log("Read Option:", $scope.feature.readOption);
            console.log("Read/Write Option:", $scope.feature.readWriteOption);

            $http({
                method: 'POST',
                url: 'http://localhost:8080/api/features/',
                data: $scope.feature,
            }).then(function(response) {
                $('.modal-backdrop').hide();
                $route.reload();
            }, function(errResponse) {
                $scope.errorMessage = errResponse.data.errorMessage;
            });
        }

        $scope.submitFeatureForm = function(featureId)
        {
            console.log("****feature id after form submission *** is : " + $scope.feature.id);
            console.log("****feature name after form submission *** is : " + $scope.feature.name);

            $scope.featureId = featureId;

            $scope.feature.readOption = $scope.feature.readOption ? 1 : 0;
            $scope.feature.readWriteOption = $scope.feature.readWriteOption ? 1 : 0;

            console.log("Updated Read Option:", $scope.feature.readOption);
            console.log("Updated Read/Write Option:", $scope.feature.readWriteOption);

            $http({
                method: 'PUT',
                url: 'http://localhost:8080/api/features/' + featureId,
                data: $scope.feature
            }).then(function(response) {
                console.log("Updated Feature data..." + JSON.stringify(response.data));
                $('.modal-backdrop').hide();
                $route.reload();
            }, function(errResponse) {
                $scope.errorMessage = "Error while updating Feature - Error Message: '" + errResponse.data.errorMessage;
            });
        }

        $scope.deleteFeature = function(featureId)
        {
            $http({
                method: 'DELETE',
                url: 'http://localhost:8080/api/features/' + featureId
            }).then(function(response) {
                $location.path("/list-all-features");
                $route.reload();
            });
        }

        $scope.clearFeature = function() {
            $scope.feature = {}; // Clear feature object
        };
    }
});

app.controller('listRoleController', function($rootScope,$scope, $http, $location, $route) {

    console.log("Inside of listRoleController...");

    if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

        console.log("Passed the condition inside the listRoleController");

        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/role/'
        })
            .then(
                function (response) {

                    console.log(JSON.stringify(response.data));

                    $scope.roles = response.data;

                });

        $scope.editRole = function (roleId) {

            if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

                console.log("I am inside the editUser of listRoleController to update the role");

                $scope.roless = $rootScope.roleNames;

                $scope.roleId = roleId;

                console.log("roleId is : " + $scope.roleId);

                $http({
                    method: 'GET',
                    url: 'http://localhost:8080/api/role/' + $scope.roleId
                }).then(function (response) {

                    console.log("Current data of role to edit..." + JSON.stringify(response.data));
                    $scope.role = response.data;
                    $scope.selectedRoleId = $scope.role.id;
                    console.log("CurrentRole..." + $scope.selectedItemvalue);
                    console.log("Current data of role..." + JSON.stringify(response.data));
                });
            } else {
                $location.path("/login");
                $scope.loginerror = true;
            }
        }

        $scope.registerRoleForm = function () {

            console.log("inside of submitRoleForm of listRoleController...");

            $http({
                method: 'POST',
                url: 'http://localhost:8080/api/role/',
                data: $scope.role,
            }).then(function (response) {
                //$location.path("/list-all-users");
                $('.modal-backdrop').hide();
                $route.reload();
            }, function (errResponse) {
                $scope.errorMessage = errResponse.data.errorMessage;
            });
        }

        $scope.submitRoleForm = function (roleId) {

            console.log("****role id after form submition 1 *** is : " +  $scope.role.id);
            console.log("****role name after form submition 1 *** is : " +  $scope.role.name);

            $http({
                method: 'PUT',
                url: 'http://localhost:8080/api/role/' + roleId,
                data: $scope.role
            }).then(function (response) {

                    console.log("Updated Role data..."+ JSON.stringify(response.data));
                    $('.modal-backdrop').hide();
                    //$location.path("/list-all-users");
                    $route.reload();
                },
                function (errResponse) {
                    $scope.errorMessage = "Error while updating Role - Error Message: '" + errResponse.data.errorMessage;
                });
        }

        $scope.deleteRole = function (roleId) {
            $http({
                method: 'DELETE',
                url: 'http://localhost:8080/api/role/' + roleId
            })
                .then(
                    function (response) {
                        $location.path("/list-all-roles");
                        $route.reload();
                    });
        }


        $scope.clearRole = function() {
            $scope.role = {}; // Clear role object
            $scope.roless = $rootScope.roleNames;
        };


        $scope.showTabs = function (roleId) {

            if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

                console.log("I am inside the showTabs of listRoleController to update the tabs");

                $scope.roleId = roleId;


                $http({
                    method: 'GET',
                    url: 'http://localhost:8080/api/roletabs/' + $scope.roleId
                }).then(function (response) {

                    console.log("Current data of tabs to edit..." + JSON.stringify(response.data));

                    $scope.roletabs = response.data;

                    console.log("Available Tabs..." + JSON.stringify($scope.roletabs.availableTab));
                    console.log("Assigned Tabs..." + JSON.stringify($scope.roletabs.assignedTab));

                });
            } else {
                $location.path("/login");
                $scope.loginerror = true;
            }

        }

        $scope.available = [];
        $scope.selected = [];

        $scope.moveItem = function(itemsToMove, sourceList, targetList) {
            if (itemsToMove && itemsToMove.length > 0) {
                for (var i = 0; i < itemsToMove.length; i++) {
                    var item = itemsToMove[i];

                    console.log("tab which is selected and passed : " + item.name);

                    for (var j = 0; j < sourceList.length; j++) {
                        if (sourceList[j].id === item.id) {

                            sourceList.splice(j, 1);
                            targetList.push(item);
                            break;
                        }
                    }
                }
            }
            $scope.available = [];
            $scope.selected = [];
        };


            $scope.moveAll = function(sourceList, targetList) {
                $scope.moveItem(sourceList.slice(), sourceList, targetList);
            };

            $scope.roletabs = {availableTab: [], assignedTab: []};
            console.log("Final roletabs is : " + $scope.roletabs)

            $scope.closeRoleTab = function (){

                $scope.roletabs.availableTab = null;
                $scope.roletabs.assignedTab = null;

            }

    }

            $scope.updateTabFeature = function (assignedTab) {

                console.log("update RoleTabs of listRoleController...");

                $scope.assignedTabs=[];

/*
                angular.forEach(assignedTab, function(item) {
                    console.log("Assigned Tabs Object :" + item);
                    $scope.assignedTabs.add(item);
                    console.log("assignedTabs inside foreach :" +item);
                });
*/

                for (var j = 0; j < assignedTab.length; j++) {

                    var item = assignedTab[j];
                    $scope.assignedTabs.push(item);

                }

                console.log("scope.assignedTabs :" + $scope.assignedTabs);

                $http({
                    method: 'PUT',
                    url: 'http://localhost:8080/api/roletabs/' + parseInt($scope.roleId),
                    data:$scope.assignedTabs
                }).then(function (response) {

                       $scope.farshid= response.data;

                }, function (errResponse) {
                    $scope.errorMessage = errResponse.data.errorMessage;
                });
            }

});


app.controller('listTabController', function($rootScope, $scope, $http, $location, $route) {

    console.log("Inside of listTabController...");

    if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {

        console.log("Passed the condition inside the listTabController");

        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/tab/'
        }).then(function(response) {
            console.log(JSON.stringify(response.data));
            $scope.tabs = response.data;
        });

        $scope.editTab = function(tabId) {
            if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {
                console.log("I am inside the editTab of listTabController to update the tab");

                $scope.tabId = tabId;

                $http({
                    method: 'GET',
                    url: 'http://localhost:8080/api/tab/' + $scope.tabId
                }).then(function(response) {
                    console.log("Current data of tab to edit..." + JSON.stringify(response.data));
                    $scope.tab = response.data;
                });
            } else {
                $location.path("/login");
                $scope.loginerror = true;
            }
        }

        $scope.registerTabForm = function() {
            console.log("inside of registerTabForm of listTabController...");

            $http({
                method: 'POST',
                url: 'http://localhost:8080/api/tab/',
                data: $scope.tab,
            }).then(function(response) {
                $('.modal-backdrop').hide();
                $route.reload();
            }, function(errResponse) {
                $scope.errorMessage = errResponse.data.errorMessage;
            });
        }

        $scope.submitTabForm = function(tabId) {
            console.log("****tab id after form submission *** is : " + $scope.tab.id);
            console.log("****tab name after form submission *** is : " + $scope.tab.name);

            $http({
                method: 'PUT',
                url: 'http://localhost:8080/api/tab/' + tabId,
                data: $scope.tab
            }).then(function(response) {
                console.log("Updated Tab data..." + JSON.stringify(response.data));
                $('.modal-backdrop').hide();
                $route.reload();
            }, function(errResponse) {
                $scope.errorMessage = "Error while updating Tab - Error Message: '" + errResponse.data.errorMessage;
            });
        }

        $scope.deleteTab = function(tabId) {
            $http({
                method: 'DELETE',
                url: 'http://localhost:8080/api/tab/' + tabId
            }).then(function(response) {
                $location.path("/list-all-tabs");
                $route.reload();
            });
        }

        $scope.clearTab = function() {
            $scope.tab = {}; // Clear tab object
        };

        $scope.showFeatures = function(tabId) {
            if ($rootScope.authenticated && $rootScope.currentRole == "ADMIN") {
                console.log("I am inside the showFeatures of listTabController to update the features");

                $scope.tabId = tabId;

                $http({
                    method: 'GET',
                    url: 'http://localhost:8080/api/tabfeatures/' + $scope.tabId
                }).then(function(response) {
                    console.log("Current data of features to edit..." + JSON.stringify(response.data));

                    $scope.tabfeatures = response.data;

                    console.log("Available Features..." + JSON.stringify($scope.tabfeatures.availableFeatures));
                    console.log("Assigned Features..." + JSON.stringify($scope.tabfeatures.assignedFeatures));

                });
            } else {
                $location.path("/login");
                $scope.loginerror = true;
            }
        }

        $scope.available = [];
        $scope.selected = [];

        $scope.moveItem = function(itemsToMove, sourceList, targetList) {
            if (itemsToMove && itemsToMove.length > 0) {
                for (var i = 0; i < itemsToMove.length; i++) {
                    var item = itemsToMove[i];

                    console.log("feature which is selected and passed : " + item.name);

                    for (var j = 0; j < sourceList.length; j++) {
                        if (sourceList[j].id === item.id) {
                            sourceList.splice(j, 1);
                            targetList.push(item);
                            break;
                        }
                    }
                }
            }
            $scope.available = [];
            $scope.selected = [];
        };

        $scope.moveAll = function(sourceList, targetList) {
            $scope.moveItem(sourceList.slice(), sourceList, targetList);
        };

        $scope.tabfeatures = { availableFeatures: [], assignedFeatures: [] };

        $scope.closeFeatureModal = function() {
            $scope.tabfeatures.availableFeatures = null;
            $scope.tabfeatures.assignedFeatures = null;
        }

        $scope.updateFeatureTab = function(assignedFeatures) {
            console.log("update FeatureTab of listTabController...");

            $scope.assignedFeatures = [];

            for (var j = 0; j < assignedFeatures.length; j++) {
                var item = assignedFeatures[j];
                $scope.assignedFeatures.push(item);
            }

            console.log("scope.assignedFeatures :" + $scope.assignedFeatures);

            $http({
                method: 'PUT',
                url: 'http://localhost:8080/api/tabfeatures/' + parseInt($scope.tabId),
                data: $scope.assignedFeatures
            }).then(function(response)
            {
                $scope.updatedFeatures = response.data;
            }, function(errResponse) {
                $scope.errorMessage = errResponse.data.errorMessage;
            });
        }
    }
});
