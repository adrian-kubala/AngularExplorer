(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('RootController', RootController);

    RootController.$inject = ['Root'];

    function RootController(Root) {

        var vm = this;

        vm.roots = [];

        loadAll();

        function loadAll() {
            Root.query(function(result) {
                vm.roots = result;
                vm.searchQuery = null;
            });
        }
    }
})();
