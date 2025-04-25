(function () {
    'use strict';

    angular.module(
        'nfseApp',
        [
            'LocalStorageModule',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ui.bootstrap',
            'ngResource',
            'ui.router',
            'ngCookies',
            'ngCacheBuster',
            'infinite-scroll',
            'ui.utils.masks',
            'ncy-angular-breadcrumb',
            'cgNotify',
            'oitozero.ngSweetAlert',
            'idf.br-filters',
            'summernote',
            'ngIdle',
            'ngImgCrop',
            'AngularPrint',
            'ui.codemirror',
            'ngFileUpload',
            'vcRecaptcha',
            'angularMoment',
            'youtube-embed',
            'ngSanitize',
            'chart.js',
            'prettyXml'
        ])

        .run(function ($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, VERSION) {
            $rootScope.VERSION = VERSION;
            $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
                $rootScope.toState = toState;
                $rootScope.toStateParams = toStateParams;

                if (Principal.isIdentityResolved()) {
                    Auth.authorize();
                }

                // Update the language
                //Language.getCurrent().then(function (language) {
                $translate.use("pt-br");
                // });

            });
            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams, localStorageService) {
                var titleKey = 'global.title';
                $rootScope.previousStateName = fromState.name;
                $rootScope.previousStateParams = fromParams;
                // Set the page title key to the one configured in state or use default one
                if (toState.data.pageTitle) {
                    titleKey = toState.data.pageTitle;
                }
                $translate(titleKey).then(function (title) {
                    // Change window title with translated one
                    $window.document.title = title;
                });

                if (toState.name === 'credenciamento' || toState.name === 'documentacaoCredenciamento') {
                    if (localStorageService && localStorageService.get("prestadorPrincipal") && localStorageService.get('prestadorPrincipal').prestador) {
                        $state.go('home');
                    }
                }

                if ((fromState.name === 'login' && (toState.name === 'password'))) {
                    $state.go('home');
                }

                document.body.scrollTop = document.documentElement.scrollTop = 0;

            });

            $rootScope.back = function () {
                // If previous state is 'activate' or do not exist go to 'home'
                if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                    $state.go('home', {}, {reload: true});
                } else {
                    $state.go($rootScope.previousStateName, $rootScope.previousStateParams, {reload: true});
                }
            };
        })
        .factory('authInterceptor', function ($rootScope, $q, $location, localStorageService) {
            return {
                // Add authorization token to headers
                request: function (config) {
                    config.headers = config.headers || {};
                    var token = localStorageService.get('token');
                    if (token && token.expires_at && token.expires_at > new Date().getTime()) {
                        config.headers.Authorization = 'Bearer ' + token.access_token;
                    }
                    if (localStorageService.get('prestadorPrincipal') && localStorageService.get('prestadorPrincipal').prestador) {
                        config.headers.Prestador = localStorageService.get('prestadorPrincipal').prestador.id;
                    }
                    return config;
                }
            };
        })
        .factory('authExpiredInterceptor', function ($rootScope, $q, $injector, localStorageService) {
            return {
                responseError: function (response) {
                    var status = response.status;
                    var Notificacao = $injector.get('Notificacao');
                    var $filter = $injector.get('$filter');
                    var $state = $injector.get('$state');
                    var $timeout = $injector.get('$timeout');
                    if (status == 400) {
                        var data;
                        if (response.data instanceof ArrayBuffer) {
                            var decoder = new TextDecoder("utf-8");
                            var errorText = decoder.decode(response.data);
                            data = JSON.parse(errorText);
                        } else {
                            data = response.data;
                        }

                        if (data && data.mensagens) {
                            var valor = "";
                            for (var i = 0; i < data.mensagens.length; i++) {
                                valor += $filter("translate")(data.mensagens[i]);
                            }
                            $timeout(function () {
                                Notificacao.error(data.message, valor);
                            });
                        } else if (data && data.fieldErrors && data.fieldErrors.length > 0) {
                            var campos = "";
                            for (var i = 0; i < data.fieldErrors.length; i++) {
                                var field = data.fieldErrors[i];
                                var error = $filter("translate")('entity.errorCode.' + field.codigo);
                                var campo = "<b>" + $filter("translate")('nfseApp.' + field.entidade + '.' + field.campo) + "</b>";
                                campos = campos + " " + (campo + " - " + error + "</br>");
                            }
                            $timeout(function () {
                                Notificacao.error("Atenção", campos);
                            }, 1000);
                        } else if (data.message) {
                            $timeout(function () {
                                Notificacao.error($filter("translate")('entity.errorCode.' + data.title), data.message);
                            }, 1000);
                        } else if (data.error == 'invalid_grant') {
                            //Notificacao.error("Error - " + [status]);
                        } else {
                            $timeout(function () {
                                Notificacao.error("Error - " + [status]);
                            }, 1000);
                        }
                    }
                    if (status == 500) {
                        Notificacao.error(" Error - " + [status]);
                    }
                    if (response.status === 401 && (response.data.error == 'invalid_token' || response.data.error == 'Unauthorized')) {
                        localStorageService.remove('token');
                        var Principal = $injector.get('Principal');
                        if (Principal.isAuthenticated()) {
                            var Auth = $injector.get('Auth');
                            Auth.authorize(true);
                        }
                    } else if (response.status === 401) {
                        $state.go("home");
                    }
                    return $q.reject(response);
                }
            };
        })

        .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {
            //Cache everything except rest api requests
            httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);
            $urlRouterProvider.otherwise('/login');

            $stateProvider.state('out', {
                'abstract': true,
                views: {
                    'navbar@': {
                        templateUrl: 'app/components/navbar/navbar.html',
                        controller: 'NavbarController'
                    },
                    'menu@': {
                        templateUrl: 'app/components/menu/navigation.html',
                    },
                    'footer@': {
                        controller: 'FooterController',
                        templateUrl: 'app/components/footer/footer.html',
                    },
                    'breadcrumb@': {
                        templateUrl: 'app/components/breadcrumb/breadcrumb.html',
                    }
                },
                data: {
                    specialClass: "gray-bg"
                },
                resolve: {
                    authorize: ['Auth',
                        function (Auth) {
                            return Auth.authorize();
                        }
                    ],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        // $translatePartialLoader.addPart('global');
                    }]
                }
            });

            $stateProvider.state('site', {
                'abstract': true,
                'parent': 'out',
                data: {
                    specialClass: "gray-bg"
                },
                views: {
                    'navbar@': {
                        templateUrl: 'app/components/navbar/navbar.html',
                        controller: 'NavbarController'
                    },
                    'menu@': {
                        templateUrl: 'app/components/menu/navigation.html',
                        controller: 'NavbarController'
                    },
                    'footer@': {
                        templateUrl: 'app/components/footer/footer.html',
                        controller: 'FooterController'
                    }
                }
            });

            $stateProvider.state('external', {
                'abstract': true,
                'parent': 'out',
                data: {
                    specialClass: "gray-bg external",
                    navbarClass: 'external'
                },
                views: {
                    'navbar@': {
                        templateUrl: 'app/components/navbar/navbar.html'
                    },
                    'menu@': {
                        templateUrl: 'app/components/menu/no-navigation.html'
                    },
                    'footer@': {
                        templateUrl: 'app/components/footer/footer.html',
                        controller: 'FooterController'
                    }
                }
            });

            $httpProvider.interceptors.push('authExpiredInterceptor');
            $httpProvider.interceptors.push('authInterceptor');
            // Initialize angular-translate
            $translateProvider.useLoader('$translatePartialLoader', {
                urlTemplate: 'i18n/{lang}/{part}.json'
            });
            $translateProvider.preferredLanguage('pt-br');
            $translateProvider.useCookieStorage();
            $translateProvider.useSanitizeValueStrategy('escaped');
            tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
            tmhDynamicLocaleProvider.useCookieStorage();
            tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

        }).config(function (IdleProvider, KeepaliveProvider, $breadcrumbProvider) {
        // configure Idle settings
        // IdleProvider.idle(1500); // in seconds
        // IdleProvider.timeout(300); // in seconds
        //KeepaliveProvider.interval(10); // in seconds
        $breadcrumbProvider.setOptions({
            template: '<ol class="breadcrumb">' +
                '<li>' +
                '<a ui-sref="home">Home</a>' +
                '</li>' +
                '<li ng-repeat="step in steps" ng-class="{active: $last}" ng-switch="$last || !!step.abstract">' +
                '<a ng-switch-when="false" href="{{step.ncyBreadcrumbLink}}">{{step.ncyBreadcrumbLabel}}</a>' +
                '<span ng-switch-when="true">{{step.ncyBreadcrumbLabel}}</span>' +
                '</li>' +
                '</ol>'
        });
    })
        .filter("sanitize", ['$sce', function ($sce) {
            return function (htmlCode) {
                return $sce.trustAsHtml(htmlCode);
            }
        }])
        .run(function ($rootScope, $state, notify) {
            $rootScope.$state = $state;
            notify.config({
                duration: 10000
            });
        })
        .component('home', {
            template: '<h1>Home</h1><p>Hello!</p>',
            controller: function () {
                this.user = {name: 'world'};
            }
        });
    ;
})();
