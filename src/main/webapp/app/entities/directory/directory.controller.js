(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('DirectoryController', DirectoryController);

    DirectoryController.$inject = ['Directory'];

    function DirectoryController(Directory) {

        var vm = this;

        vm.directories = [];

        loadAll();

        function loadAll() {
            Directory.query(function(result) {
                vm.directories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
