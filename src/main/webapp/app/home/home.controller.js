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

        vm.rootId = 0;
        vm.directoryId = 0;

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

        vm.toggleRoot = toggleRoot;

        function toggleRoot(id) {
          console.log(id)
          if (id != vm.rootId) {
            return id
          } else {
            return 0
          }
        }

        vm.toggleDirectory = toggleDirectory;

        function toggleDirectory(id) {
          console.log(id)
          if (id != vm.directoryId) {
            return id
          } else {
            return 0
          }
        }
    }

})();
