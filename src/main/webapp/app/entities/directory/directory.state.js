(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('directory', {
            parent: 'entity',
            url: '/directory',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Directories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/directory/directories.html',
                    controller: 'DirectoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('directory-detail', {
            parent: 'directory',
            url: '/directory/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Directory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/directory/directory-detail.html',
                    controller: 'DirectoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Directory', function($stateParams, Directory) {
                    return Directory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'directory',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('directory-detail.edit', {
            parent: 'directory-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-dialog.html',
                    controller: 'DirectoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Directory', function(Directory) {
                            return Directory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('directory.new', {
            parent: 'directory',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-dialog.html',
                    controller: 'DirectoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('directory', null, { reload: 'directory' });
                }, function() {
                    $state.go('directory');
                });
            }]
        })
        .state('directory.edit', {
            parent: 'directory',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-dialog.html',
                    controller: 'DirectoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Directory', function(Directory) {
                            return Directory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('directory', null, { reload: 'directory' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('directory.delete', {
            parent: 'directory',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-delete-dialog.html',
                    controller: 'DirectoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Directory', function(Directory) {
                            return Directory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('directory', null, { reload: 'directory' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
