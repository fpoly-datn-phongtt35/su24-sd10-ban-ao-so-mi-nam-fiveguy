app.controller("CategoryController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.category = {};
    $scope.categoryUpdate = {};
    $scope.categories = [];
    
    $scope.getAllCategories = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $http.get(`${config.host}/category`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.categories = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
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
        $http.put(`${config.host}/category/status/${$scope.category.id}`).then(response => {
            $scope.getAllCategories();
            $scope.category = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createCategory = () => {
        if ($scope.categoryForm.$valid) {
           $http.post(`${config.host}/category`, $scope.category).then((response) => {
                $scope.getAllCategories();
                $scope.resetForm();
                $('#addCategoryModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateCategory = () => {
        if ($scope.categoryFormUpdate.$valid) {
            $http.put(`${config.host}/category/${$scope.categoryUpdate.id}`, $scope.categoryUpdate).then(response => {
                $scope.getAllCategories();
                $scope.resetFormUpdate();
                $('#editCategoryModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/category/${$scope.category.id}`).then(response => {
            $scope.getAllCategories();
            $scope.category = {};
            $('#deleteCategoryModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }


})