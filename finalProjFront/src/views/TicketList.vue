<template>
    <div class="ticket-list">
        <h2>報修單總覽</h2>

        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>標題</th>
                    <th>狀態</th>
                    <th>指派人</th>
                    <th>優先順序</th>
                    <th>建立時間</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(ticket, index) in tickets" :key="index">
                    <td>{{ ticket.id }}</td>
                    <td>{{ ticket.title }}</td>
                    <td>
                        <span class="badge" :class="statusClass(ticket.status)">
                            {{ ticket.status }}
                        </span>
                    </td>
                    <td>{{ ticket.assigner }}</td>
                    <td>
                        <span class="priority" :class="priorityClass(ticket.priority)">
                            {{ ticket.priority }}
                        </span>
                    </td>
                    <td>{{ ticket.createdAt }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
const tickets = [
    {
        id: 1,
        title: '浴室漏水',
        status: 'To Do',
        assigner: '王小明',
        priority: 'High',
        createdAt: '2025-06-28',
    },
    {
        id: 2,
        title: '電梯異常',
        status: 'In Progress',
        assigner: '李大華',
        priority: 'Medium',
        createdAt: '2025-06-25',
    },
    {
        id: 3,
        title: '門禁感應失效',
        status: 'Done',
        assigner: '系統自動',
        priority: 'Low',
        createdAt: '2025-06-20',
    },
]

function statusClass(status) {
    return {
        'status-todo': status === 'To Do',
        'status-inprogress': status === 'In Progress',
        'status-done': status === 'Done',
    }
}

function priorityClass(priority) {
    return {
        'priority-high': priority === 'High',
        'priority-medium': priority === 'Medium',
        'priority-low': priority === 'Low',
    }
}
</script>

<style scoped>
.ticket-list {
    max-width: 900px;
    margin: 0 auto;
    padding: 24px;
    font-family: Arial, sans-serif;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 16px;
}

th,
td {
    padding: 12px;
    border-bottom: 1px solid #ddd;
    text-align: left;
}

.badge {
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 13px;
    color: white;
}

.status-todo {
    background-color: #ffab00;
}

.status-inprogress {
    background-color: #0052cc;
}

.status-done {
    background-color: #36b37e;
}

.priority {
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 12px;
    color: white;
}

.priority-high {
    background-color: red;
}

.priority-medium {
    background-color: orange;
}

.priority-low {
    background-color: gray;
}
</style>