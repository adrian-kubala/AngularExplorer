(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('DirectoryDetailController', DirectoryDetailController);

    DirectoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Directory', 'Root'];

    function DirectoryDetailController($scope, $rootScope, $stateParams, previousState, entity, Directory, Root) {
        var vm = this;

        vm.directory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('angularExplorerApp:directoryUpdate', function(event, result) {
            vm.directory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
