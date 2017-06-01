(function() {
    'use strict';
    angular
        .module('angularExplorerApp')
        .factory('Root', Root);

    Root.$inject = ['$resource'];

    function Root ($resource) {
        var resourceUrl =  'api/roots/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
