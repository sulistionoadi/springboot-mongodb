'use strict';

/**
 * @ngdoc function
 * @name belajarSecuringJwtApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the belajarSecuringJwtApp
 */
angular.module('belajarSecuringJwtApp')
  .controller('MainCtrl', function ($scope, $window) {

  		$scope.init = function(){
  			var token = $window.localStorage.getItem('token');
  			$scope.username=token;
  		};

  		$scope.init();

});
