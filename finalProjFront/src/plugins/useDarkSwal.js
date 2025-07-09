import Swal from 'sweetalert2'
import '@sweetalert2/theme-dark/dark.css'

const baseOptions = {
    buttonsStyling: false,
    customClass: {
        popup: 'swal2-dark',
        title: 'fw-bold',
        confirmButton: 'btn btn-primary me-2',
        cancelButton: 'btn btn-secondary'
    }
}

export function useDarkSwal() {
    return {
        async success(title = '成功', text = '') {
            return await Swal.fire({
                icon: 'success',
                title,
                text,
                ...baseOptions
            })
        },
        async error(title = '錯誤', text = '請稍後再試') {
            return await Swal.fire({
                icon: 'error',
                title,
                text,
                ...baseOptions
            })
        },
        async confirm(title = '確定要執行？', text = '此操作無法還原！') {
            return await Swal.fire({
                icon: 'warning',
                title,
                text,
                showCancelButton: true,
                confirmButtonText: '確定',
                cancelButtonText: '取消',
                ...baseOptions
            })
        }
    }
}
