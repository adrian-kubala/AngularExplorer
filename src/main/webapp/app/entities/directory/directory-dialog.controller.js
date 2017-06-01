(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('DirectoryDialogController', DirectoryDialogController);

    DirectoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Directory', 'Root', 'File'];

    function DirectoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Directory, Root, File) {
        var vm = this;

        vm.directory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.roots = Root.query();
        vm.files = File.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.directory.id !== null) {
                Directory.update(vm.directory, onSaveSuccess, onSaveError);
            } else {
                Directory.save(vm.directory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('angularExplorerApp:directoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
