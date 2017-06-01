(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('RootDialogController', RootDialogController);

    RootDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Root', 'Directory'];

    function RootDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Root, Directory) {
        var vm = this;

        vm.root = entity;
        vm.clear = clear;
        vm.save = save;
        vm.directories = Directory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.root.id !== null) {
                Root.update(vm.root, onSaveSuccess, onSaveError);
            } else {
                Root.save(vm.root, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('angularExplorerApp:rootUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
