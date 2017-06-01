(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('FileDetailController', FileDetailController);

    FileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'File', 'Directory'];

    function FileDetailController($scope, $rootScope, $stateParams, previousState, entity, File, Directory) {
        var vm = this;

        vm.file = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('angularExplorerApp:fileUpdate', function(event, result) {
            vm.file = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
