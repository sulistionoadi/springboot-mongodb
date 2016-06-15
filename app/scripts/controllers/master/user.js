'use strict';

/**
 * @ngdoc function
 * @name belajarSecuringJwtApp.controller:MasterUserCtrl
 * @description
 * # MasterUserCtrl
 * Controller of the belajarSecuringJwtApp
 */
angular.module('belajarSecuringJwtApp')
        .controller('MasterUserCtrl', function ($scope, $timeout, UserService, RoleService) {

            $scope.paging = {
                currentPage: 1
            };
            $scope.reloadData = function () {
                RoleService.count().success(function (result) {
                    RoleService.query(0, result).success(function (data) {
                        $scope.roles = data.content;
                    });
                });

                UserService.query($scope.paging.currentPage - 1, 10).success(function (data) {
                    $scope.users = data.content;
                    $scope.paging.maxSize = (data.size);
                    $scope.paging.totalItems = data.totalElements;
                    $scope.paging.currentPage = parseInt(data.number) + 1;
                    $scope.paging.maxPage = data.totalPages;
                });
            };

            $scope.reloadData();

            $scope.edit = function (user) {
                if (user.id) {
                    UserService.get('id', user.id).success(function (data) {
                        $scope.currentUser = data;
                        $scope.original = angular.copy($scope.currentUser);
                    });
                }
                $timeout(function () {
                    $('#cmbRole').val(user.role.id);
                }, 50);
            };

            $scope.isClean = function () {
                return angular.equals($scope.original, $scope.currentUser);
            };

            $scope.clear = function () {
                $scope.currentUser = null;
                $scope.original = null;
                $scope.usernameExist = false;
                $scope.passwordRequired = false;

                $scope.reloadData();
            };

            $scope.delete = function (user) {
                bootbox.confirm('Are you sure to delete user [' + user.username + '] ?', function (result) {
                    if (result) {
                        UserService.remove(user).success(function () {
                            $scope.clear();
                        });
                    }
                });
            };

            $scope.save = function () {
                if (!$scope.currentUser.id && !$scope.currentUser.password) {
                    $scope.passwordRequired = true;
                    return;
                }

                UserService.get('username', $scope.currentUser.username).success(function (data) {
                    if (data && (data.id !== $scope.currentUser.id)) {
                        $scope.usernameExist = true;
                        return;
                    } else {
                        UserService.save($scope.currentUser).success(function () {
                            bootbox.alert('User [' + $scope.currentUser.username + '] saved ');
                            $scope.clear();
                        });
                    }
                });
            };

        });
