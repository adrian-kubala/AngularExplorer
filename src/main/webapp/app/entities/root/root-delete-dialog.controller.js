(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('RootDeleteController',RootDeleteController);

    RootDeleteController.$inject = ['$uibModalInstance', 'entity', 'Root'];

    function RootDeleteController($uibModalInstance, entity, Root) {
        var vm = this;

        vm.root = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Root.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
