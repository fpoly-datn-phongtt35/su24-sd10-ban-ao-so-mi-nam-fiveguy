app.controller("CollarController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;

    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.collar = {};
    $scope.collarUpdate = {};
    $scope.collars = [];
    
    $scope.getAllCollars = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/collar`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.collars = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchCollars = () => {
        $scope.page = 0;
        $scope.getAllCollars();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllCollars();
        }
    }

    $scope.getAllCollars();

    $scope.resetForm = () => {
        $scope.collar = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.collarUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editCollar = (key) => {
        $http.get(`${config.host}/collar/${key}`).then((response) => {
            $scope.collarUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.collar = element;
    }

    $scope.handleStatus = (element) => {
        $scope.collar = element;
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/collar/status/${$scope.collar.id}`).then(response => {
            $('#updateStatus').css('display', 'none');
            $('#loadingStatus').css('display', 'inline-block');
            $('#updateStatusModel').modal('hide');
            $scope.getAllCollars();
            $scope.collar = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'none');
            $('#loadingStatus').css('display', 'inline-block');
            console.log("Error", error);
        })
    }

    $scope.createCollar = () => {
        if ($scope.CollarForm.$valid) {
            $('#addCollar').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/collar`, $scope.collar).then((response) => {
                $('#addCollar').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addCollarModel').modal('hide');
                $scope.getAllCollars();
                $scope.resetForm();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addCollar').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateCollar = () => {
        if ($scope.CollarFormUpdate.$valid) {
            $('#updateCollar').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/collar/${$scope.collarUpdate.id}`, $scope.collarUpdate).then(response => {
                $('#updateCollar').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editCollarModal').modal('hide');
                $scope.getAllCollars();
                $scope.resetFormUpdate();
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                $('#updateCollar').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $('#deleteCollar').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/collar/${$scope.collar.id}`).then(response => {
            $('#deleteCollar').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $scope.getAllCollars();
            $scope.collar = {};
            $('#deleteCollarModel').modal('hide');
            toastr["success"]("Ngừng hoạt động " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteCollar').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            toastr["error"](error);
        })
    }


})