'use strict';

/**
 * @ngdoc service
 * @name belajarSecuringJwtApp.RoleService
 * @description
 * # RoleService
 * Factory in the belajarSecuringJwtApp.
 */
angular.module('belajarSecuringJwtApp')
        .factory('RoleService', function ($http) {
            return {
                count: function () {
                    return $http.get('/api/role/count');
                },
                query: function (page, size) {
                    if (!page) {
                        page = 0;
                    }
                    if (!size) {
                        size = 10;
                    }

                    return $http.get('/api/role/', {
                        params: {'page': page, 'size': size}
                    });
                },
                get: function (column, value) {
                    return $http.get('/api/role/' + column + '/' + value);
                },
                save: function (obj) {
                    if (obj.id) {
                        return $http.put('/api/role/' + obj.id, obj);
                    } else {
                        return $http.post('/api/role/', obj);
                    }
                },
                remove: function (obj) {
                    if (obj.id) {
                        return $http.delete('/api/role/' + obj.id);
                    }
                }
            };
        });
