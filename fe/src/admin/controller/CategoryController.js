app.controller("CategoryController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC',
    };
    $scope.category = {};
    $scope.categoryUpdate = {};
    $scope.categories = [];
    
    $scope.getAllCategories = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/category`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status,
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.categories = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchCategories = () => {
        $scope.page = 0;
        $scope.getAllCategories();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllCategories();
        }
    }

    $scope.getAllCategories();

    $scope.resetForm = () => {
        $scope.category = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.categoryUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editCategory = (key) => {
        $http.get(`${config.host}/category/${key}`).then((response) => {
            $scope.categoryUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.category = element;
    }

    $scope.handleStatus = (element) => {
        $scope.category = element;
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/category/status/${$scope.category.id}`).then(response => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            $('#updateStatusModel').modal('hide');
            $scope.getAllCategories();
            $scope.category = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            console.log("Error", error);
        })
    }

    $scope.createCategory = () => {
        if ($scope.categoryForm.$valid) {
            $('#addCategory').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/category`, $scope.category).then((response) => {
                $('#addCategory').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addCategoryModel').modal('hide');
                $scope.getAllCategories();
                $scope.resetForm();
                
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addCategory').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateCategory = () => {
        if ($scope.categoryFormUpdate.$valid) {
            $('#updateCategory').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/category/${$scope.categoryUpdate.id}`, $scope.categoryUpdate).then(response => {
                $('#updateCategory').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editCategoryModal').modal('hide');
                $scope.getAllCategories();
                $scope.resetFormUpdate();           
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                $('#updateCategory').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $('#deleteCategory').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/category/${$scope.category.id}`).then(response => {
            $('#deleteCategory').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $('#deleteCategoryModel').modal('hide');
            $scope.getAllCategories();
            $scope.category = {};
            toastr["success"]("Ngừng hoạt động " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteCategory').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            toastr["error"](error);
        })
    }


})