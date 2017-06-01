(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['Root', 'Directory', 'File'];

    function HomeController (Root, Directory, File) {
        var vm = this;

        vm.roots = [];
        vm.directories = [];
        vm.files = [];

        getData();

        function getData() {

          Root.query(function(result) {
            vm.roots = result;
            vm.searchQuery = null;
          });

          Directory.query(function(result) {
            vm.directories = result;
            vm.searchQuery = null;
          });

          File.query(function(result) {
            vm.files = result;
            vm.searchQuery = null;
          });
        }
    }

})();
