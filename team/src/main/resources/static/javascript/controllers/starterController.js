angular.module('urlShortener')

    .controller('starterCtrl', ['$scope', '$state', 'urlShortener', function ($scope, $state, urlShortener) {

        $scope.url = "";    // initial url input form
        $scope.qr = "https://66.media.tumblr.com/44e89309ea155b3be1213e64cc872f2a/tumblr_n0wqfhEW9K1sghdp8o1_400.gif";
        $scope.avaiableQR = false;
        $scope.regionAvaiable = true;
        $scope.wantQr = false;

        // variables for vcard/qr generator
        $scope.qrFName = "";
        $scope.qrLName = "";
        $scope.qrEmail = "";
        $scope.qrPhone = "";
        $scope.qrCompany = "";
        $scope.qrStreet = "";
        $scope.qrZip = "";
        $scope.qrCity = "";
        $scope.qrCountry = "";
        $scope.qrLevel = "L";
        $scope.qrColour = "Black";
        $scope.qrLogo = "";

        // FEEDBACK MESSAGES

        // feedback handling variables
        $scope.error = false;
        $scope.success = false;
        $scope.successMsg = "";
        $scope.errorMsg = "";
        $scope.safe = true;

        // hide the error mensage
        $scope.hideError = function () {
            $scope.errorMsg = "";
            $scope.error = false;
        };
        // show the error mensage
        var showError = function (error) {
            $scope.errorMsg = error;
            $scope.error = true;
        };

        $scope.hideSafe = function () {
            $scope.safe = true;
        };

        // show the success mensage
        var showSuccess = function (message) {
            $scope.successMsg = message.uri;
            $scope.success = true;
            $scope.safe = message.safe;

            if($scope.wantQr.toString() == 'true'){
                $scope.qr = "data:image/png;base64," + message.qrCode;
            }
            $scope.avaiableQR = true;
        };

        // hide the success mensage
        $scope.hideSuccess = function () {
            $scope.success = false;
            $scope.successMsg = "";
        };

        // hide the generated Qr
        $scope.hideImage = function () {
            $scope.avaiableQR = false;
            $scope.qr = "";
        };

        $scope.shortURL = function () {
                    if ($scope.wantQr.toString() == 'false') {
                        var url = {
                            url: $scope.url,
                            safe: $scope.safe,
                            wantQr: 'false'
                        };

                        urlShortener.shortURL(url, showSuccess, showError);
                    }else{
                        var url = {
                            url: $scope.url,
                            safe: $scope.safe,
                            wantQr: 'true',
                            fName:  "" + $scope.qrFName,
                            lName: "" + $scope.qrLName,
                            Email:  "" + $scope.qrEmail,
                            Phone: "" + $scope.qrPhone,
                            Company: "" + $scope.qrCompany,
                            Street: "" + $scope.qrStreet,
                            Zip: "" + $scope.qrZip,
                            City: "" + $scope.qrCity,
                            Country: "" + $scope.qrCountry,
                            Level: "" + $scope.qrLevel,
                            Colour: "" + $scope.qrColour,
                            Logo: "" + $scope.qrLogo
                        };

                        urlShortener.shortURL(url, showSuccess, showError);
                    };
        };

    }]);
