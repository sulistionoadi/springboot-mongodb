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
  })
  .service('APIInterceptor', function ($q, $location, $window) {
      var service = this;

      service.request = function (config) {
        if(config.method === 'POST' || config.method === 'PUT') {
          console.log(config);
          var oHeader = {'alg': 'HS512', 'typ':'JWT'};
          var oPayload = {};

          if(config.data) oPayload=config.data;

          var sHeader = JSON.stringify(oHeader);
          var sPayload = JSON.stringify(oPayload);
          var sJWT = KJUR.jws.JWS.sign("HS512", sHeader, sPayload, "bismillah");

          var isValid = KJUR.jws.JWS.verifyJWT(sJWT, "bismillah", {alg: ['HS512']});

          console.log("Is it valid ?", isValid);

          //if(config.data) config.data={'payload':sJWT};
          if(config.data) config.data=sJWT;
        }
        
        return config;
      };
  })
  .config(function ($httpProvider) {
      $httpProvider.interceptors.push('APIInterceptor');
  });
