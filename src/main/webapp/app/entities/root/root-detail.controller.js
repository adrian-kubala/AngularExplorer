(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('RootDetailController', RootDetailController);

    RootDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Root', 'Directory'];

    function RootDetailController($scope, $rootScope, $stateParams, previousState, entity, Root, Directory) {
        var vm = this;

        vm.root = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('angularExplorerApp:rootUpdate', function(event, result) {
            vm.root = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
