'use strict';

describe('Controller: MasterUserCtrl', function () {

  // load the controller's module
  beforeEach(module('belajarSecuringJwtApp'));

  var MasterUserCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MasterUserCtrl = $controller('MasterUserCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(MasterUserCtrl.awesomeThings.length).toBe(3);
  });
});
