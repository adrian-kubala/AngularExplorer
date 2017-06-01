'use strict';

describe('Controller Tests', function() {

    describe('Directory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDirectory, MockRoot, MockFile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDirectory = jasmine.createSpy('MockDirectory');
            MockRoot = jasmine.createSpy('MockRoot');
            MockFile = jasmine.createSpy('MockFile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Directory': MockDirectory,
                'Root': MockRoot,
                'File': MockFile
            };
            createController = function() {
                $injector.get('$controller')("DirectoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'angularExplorerApp:directoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
