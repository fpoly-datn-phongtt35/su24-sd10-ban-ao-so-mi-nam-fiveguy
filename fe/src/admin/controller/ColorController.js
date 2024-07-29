app.controller("ColorController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.color = {};
    $scope.colorUpdate = {};
    $scope.colors = [];
    
    $scope.getAllColors = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/color`, 
                {params: {page: $scope.page, size: $scope.size, keyword: $scope.filter.keyword,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.colors = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchColors = () => {
        $scope.page = 0;
        $scope.getAllColors();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllColors();
        }
    }

    $scope.getAllColors();

    $scope.resetForm = () => {
        $scope.color = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.colorUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editColor = (key) => {
        $http.get(`${config.host}/color/${key}`).then((response) => {
            $scope.colorUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.color = element;
    }

    $scope.handleStatus = (element) => {
        $scope.color = element;
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/color/status/${$scope.color.id}`).then(response => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            $('#updateStatusModel').modal('hide');
            $scope.getAllColors();
            $scope.color = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            console.log("Error", error);
        })
    }

    $scope.createColor = () => {
        if ($scope.colorForm.$valid) {
            $('#addColor').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/color`, $scope.color).then((response) => {
                $('#addColor').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addColorModel').modal('hide');
                $scope.getAllColors();
                $scope.resetForm();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addColor').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateColor = () => {
        if ($scope.colorFormUpdate.$valid) {
            $('#updateColor').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/color/${$scope.colorUpdate.id}`, $scope.colorUpdate).then(response => {
                $('#updateColor').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editColorModal').modal('hide');
                $scope.getAllColors();
                $scope.resetFormUpdate();
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                $('#updateColor').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $('#deleteColor').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/color/${$scope.color.id}`).then(response => {
            $('#deleteColor').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $('#deleteColorModel').modal('hide');
            $scope.getAllColors();
            $scope.color = {};
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteColor').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            toastr["error"](error);
        })
    }


})