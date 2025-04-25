(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('CacheController', function ($scope, Notificacao, NotaFiscal, Mongo, CacheSistema, ParseLinks) {

            $scope.activeTab;
            $scope.dataBaseMongoDB;
            $scope.collectionsMongoDB = [];
            $scope.cacheGeracaoXml = [];
            $scope.cacheSistema = {
                page: 0,
                per_page: 10,
                links: '',
                dados: []
            }

            $scope.loadCacheSistema = function () {
                $scope.activeTab = 0;
                CacheSistema.buscarCaches({
                    offset: $scope.cacheSistema.page,
                    limit: $scope.cacheSistema.per_page,
                    parametrosQuery: []
                }, function (data, headers) {
                    $scope.cacheSistema.dados = data;
                    $scope.cacheSistema.links = ParseLinks.parse(headers('link'));
                })
            }

            $scope.loadCacheSistema();

            $scope.loadPageCacheSistema = function (page) {
                $scope.cacheSistema.page = page;
                $scope.loadCacheSistema();
            };

            $scope.removerCacheSistema = function (cache) {
                Notificacao.confirmDelete(
                    function () {
                        CacheSistema.delete({id: cache.uuid}, function () {
                            $scope.loadCacheSistema();
                        });
                    }
                );
            }

            $scope.loadCacheMongoDB = function () {
                $scope.activeTab = 1;
                Mongo.getDb(null, function (data) {
                    $scope.dataBaseMongoDB = data;
                });
                Mongo.getCollections(null, function (data) {
                    $scope.collectionsMongoDB = data;
                });
            }

            $scope.dropCollectionMongoDB = function (name) {
                Notificacao.confirmDelete(
                    function () {
                        Mongo.dropCollection({collectionName: name}, function () {
                            $scope.loadCacheMongoDB();
                            Notificacao.info("Informação!", "Collection removida com sucesso!");
                        });
                    });
            };

            $scope.dropDataBaseMongoDB = function () {
                Notificacao.confirmDelete(
                    function () {
                        Mongo.dropDb({dbName: $scope.dataBaseMongoDB.name}, function () {
                            $scope.loadCacheMongoDB();
                            Notificacao.info("Informação!", "Database removido com sucesso!");
                        });
                    });
            };

            $scope.loadCacheGeracaoXml = function () {
                $scope.activeTab = 2;
                NotaFiscal.cacheGeracaoXml({}, function (data) {
                    $scope.cacheGeracaoXml = data;
                })
            }

            $scope.removerCacheGeracaoXml = function (inscricaoCadastral) {
                Notificacao.confirmDelete(
                    function () {
                        NotaFiscal.removeCacheGeracaoXml({inscricaoCadastral: inscricaoCadastral}, function () {
                            $scope.loadCacheGeracaoXml();
                        });
                    }
                );
            }
        });
})();
