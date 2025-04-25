(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('MensagemContribuinteUsuarioNewController', function ($scope, $state, entity,
                                                                          MensagemContribuinteUsuarioService,
                                                                          Principal, $sanitize) {

            $scope.entity = entity;
            $scope.declaro = false;

            $scope.baixouArquivo = function (arquivo) {
                arquivo.baixou = true;
            }

            $scope.trustAsHtml = function (value) {
                return $sanitize(value);
            };

            $scope.uploadFile = function (file, documento) {
                var reader = new FileReader();
                reader.onload = function (evt) {
                    $scope.$apply(function ($scope) {
                        documento.file = evt.target.result;
                        documento.fileName = file.name;
                    });
                };
                reader.readAsDataURL(file);
            };

            $scope.isQuestionamento = function () {
                return $scope.entity.mensagem.tipo == 'QUESTIONAMENTO';
            }

            $scope.isQuestionamentoRespondido = function () {
                return $scope.entity.resposta;
            }

            $scope.isQuestionamentoDocAnexados = function () {
                for (var i = 0; i < $scope.entity.documentos.length; i++) {
                    if ($scope.entity.documentos[i].documento.obrigatorio == true &&
                        !$scope.entity.documentos[i].file) {
                        return false;
                    }
                }
                return true;
            }

            $scope.baixouAnexos = function () {
                if ($scope.entity.mensagem.detentorArquivoComposicao &&
                    $scope.entity.mensagem.detentorArquivoComposicao.arquivos) {
                    for (var i = 0; i < $scope.entity.mensagem.detentorArquivoComposicao.arquivos.length; i++) {
                        if (!$scope.entity.mensagem.detentorArquivoComposicao.arquivos[i].baixou) {
                            return false;
                        }
                    }
                }
                return true;
            }

            $scope.habilitaBotaoConfirmar = function () {
                if ($scope.isQuestionamento() &&
                    (!$scope.isQuestionamentoRespondido() || !$scope.isQuestionamentoDocAnexados())) {
                    return false;
                }
                if (!$scope.baixouAnexos()) {
                    return false;
                }
                if (!$scope.declaro) {
                    return false;
                }
                return true;
            }

            $scope.confirmar = function () {
                Principal.identity().then(function (account) {
                    $scope.entity.lidaPor = account;
                    MensagemContribuinteUsuarioService.marcarComoLida($scope.entity, function () {
                        MensagemContribuinteUsuarioService.proximaMensagemNaoLida({}, function (data) {
                            if (data && data.id) {
                                $state.go("mensagemContribuinteUsuarioNew", {id: data.id});
                            } else {
                                $scope.$emit('nfseApp:navbarUpdate');
                                $state.go('home', {reload: true});
                            }
                        });
                    });
                });
            }
        });
})();
