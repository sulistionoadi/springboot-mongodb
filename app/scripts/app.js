'use strict';

/**
 * @ngdoc overview
 * @name belajarSecuringJwtApp
 * @description
 * # belajarSecuringJwtApp
 *
 * Main module of the application.
 */
angular
  .module('belajarSecuringJwtApp', [
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/master/user', {
        templateUrl: 'views/master/user.html',
        controller: 'MasterUserCtrl',
        controllerAs: 'master/user'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
