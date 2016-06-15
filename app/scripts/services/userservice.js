'use strict';

/**
 * @ngdoc service
 * @name belajarSecuringJwtApp.UserService
 * @description
 * # UserService
 * Factory in the belajarSecuringJwtApp.
 */
angular.module('belajarSecuringJwtApp')
        .factory('UserService', function ($http) {
            return {
                query: function (page, size) {
                    if (!page) {
                        page = 0;
                    }
                    if (!size) {
                        size = 10;
                    }

                    return $http.get('/api/user/', {
                        params: {'page': page, 'size': size}
                    });
                },
                get: function (column, value) {
                    return $http.get('/api/user/' + column + '/' + value);
                },
                save: function (obj) {
                    if (obj.id) {
                        return $http.put('/api/user/' + obj.id, obj);
                    } else {
                        return $http.post('/api/user/', obj);
                    }
                },
                remove: function (obj) {
                    if (obj.id) {
                        return $http.delete('/api/user/' + obj.id);
                    }
                }
            };
        });
