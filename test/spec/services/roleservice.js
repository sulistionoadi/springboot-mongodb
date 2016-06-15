'use strict';

describe('Service: RoleService', function () {

  // load the service's module
  beforeEach(module('belajarSecuringJwtApp'));

  // instantiate service
  var RoleService;
  beforeEach(inject(function (_RoleService_) {
    RoleService = _RoleService_;
  }));

  it('should do something', function () {
    expect(!!RoleService).toBe(true);
  });

});
