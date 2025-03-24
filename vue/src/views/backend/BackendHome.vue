<template>
  <div class="dashboard">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div class="card-header">
            <span>总房源数</span>
          </div>
          <div class="card-body">
            {{ statistics.totalHouses }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="card-header">
            <span>总用户数</span>
          </div>
          <div class="card-body">
            {{ statistics.totalUsers }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="card-header">
            <span>总收藏数</span>
          </div>
          <div class="card-body">
            {{ statistics.totalFavorites }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="card-header">
            <span>总租约数</span>
          </div>
          <div class="card-body">
            {{ statistics.totalRentals }}
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card>
          <div ref="monthlyStatsChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card >
          <div ref="distributionChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import request from '@/utils/request'

export default {
  name: 'BackendHome',
  data() {
    return {
      statistics: {
        totalHouses: 0,
        totalUsers: 0,
        totalFavorites: 0,
        totalRentals: 0,
        newUsersThisMonth: 0,
        newRentalsThisMonth: 0
      },
      monthlyStatsChart: null,
      distributionChart: null
    }
  },
  mounted() {
    this.fetchStatistics()
    this.initCharts()
  },
  methods: {
    async fetchStatistics() {
      try {
        const res = await request.get('/statistics/overview')
        console.log('统计数据:', res.data)
        this.statistics = res.data
        this.updateCharts()
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    initCharts() {
      this.monthlyStatsChart = echarts.init(this.$refs.monthlyStatsChart)
      this.distributionChart = echarts.init(this.$refs.distributionChart)
    },
    updateCharts() {
      // 月度统计图表
      this.monthlyStatsChart.setOption({
        title: {
          text: '本月新增统计',
          left: 'center',
          top: 0
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['新增用户', '新增租约'],
          bottom: 0
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['本月'],
          axisLabel: {
            interval: 0,
            rotate: 0
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '新增用户',
            type: 'bar',
            data: [this.statistics.newUsersThisMonth],
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '新增租约',
            type: 'bar',
            data: [this.statistics.newRentalsThisMonth],
            itemStyle: {
              color: '#67C23A'
            }
          }
        ]
      })

      // 数据分布图表
      this.distributionChart.setOption({
        title: {
          text: '数据分布',
          left: 'center',
          top: 0
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle'
        },
        series: [
          {
            name: '数据分布',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '20',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: this.statistics.totalHouses, name: '房源', itemStyle: { color: '#409EFF' } },
              { value: this.statistics.totalUsers, name: '用户', itemStyle: { color: '#67C23A' } },
              { value: this.statistics.totalFavorites, name: '收藏', itemStyle: { color: '#E6A23C' } },
              { value: this.statistics.totalRentals, name: '租约', itemStyle: { color: '#F56C6C' } }
            ]
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
.card-header {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}
.card-body {
  font-size: 24px;
  color: #409EFF;
}
.chart-row {
  margin-top: 20px;
}
</style>