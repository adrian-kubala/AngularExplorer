(function() {
    'use strict';

    angular
        .module('angularExplorerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('root', {
            parent: 'entity',
            url: '/root',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Roots'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/root/roots.html',
                    controller: 'RootController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('root-detail', {
            parent: 'root',
            url: '/root/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Root'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/root/root-detail.html',
                    controller: 'RootDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Root', function($stateParams, Root) {
                    return Root.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'root',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('root-detail.edit', {
            parent: 'root-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/root/root-dialog.html',
                    controller: 'RootDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Root', function(Root) {
                            return Root.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('root.new', {
            parent: 'root',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/root/root-dialog.html',
                    controller: 'RootDialogController',
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
                    $state.go('root', null, { reload: 'root' });
                }, function() {
                    $state.go('root');
                });
            }]
        })
        .state('root.edit', {
            parent: 'root',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/root/root-dialog.html',
                    controller: 'RootDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Root', function(Root) {
                            return Root.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('root', null, { reload: 'root' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('root.delete', {
            parent: 'root',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/root/root-delete-dialog.html',
                    controller: 'RootDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Root', function(Root) {
                            return Root.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('root', null, { reload: 'root' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
