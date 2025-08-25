<template>
    <div>
        <app-head></app-head>
        <app-body>
            <div style="min-height: 85vh;">
            <el-tabs v-model="labelName" type="card" @tab-click="handleClick">
                <el-tab-pane label="全部" name="0"></el-tab-pane>
                <el-tab-pane label="数码" name="1"></el-tab-pane>
                <el-tab-pane label="家电" name="2"></el-tab-pane>
                <el-tab-pane label="户外" name="3"></el-tab-pane>
                <el-tab-pane label="图书" name="4"></el-tab-pane>
                <el-tab-pane label="其他" name="5"></el-tab-pane>
            </el-tabs>

             <!--<el-menu  mode="horizontal" @select="handleSelect">
                    <el-menu-item >全部</el-menu-item>
                    <el-submenu ><template slot="title">数码</template></el-submenu>
                    <el-submenu><template slot="title">家电</template></el-submenu>
                    <el-submenu><template slot="title">户外</template></el-submenu>
                    <el-submenu><template slot="title">图书</template></el-submenu>
                    <el-submenu><template slot="title">其他</template></el-submenu>

              </el-menu>-->
            <div style="margin: 0 20px;">
                <el-row :gutter="30">
                    <el-col :span="6" v-for="(idle,index) in idleList" :key="idle.id || index">
                        <div class="idle-card" @click="toDetails(idle)">
                            <el-image
                                    style="width: 100%; height: 160px"
                                    :src="idle.imgUrl"
                                    fit="contain">
                                <div slot="error" class="image-slot">
                                    <i class="el-icon-picture-outline">无图</i>
                                </div>
                            </el-image>
                            <div class="idle-title">
                                {{idle.idleName}}
                            </div>
                            <el-row style="margin: 5px 10px;">
                                <el-col :span="12">
                                    <div class="idle-prive">￥{{idle.idlePrice}}</div>
                                </el-col>
                                <el-col :span="12">
                                    <div class="idle-place">{{idle.idlePlace}}</div>
                                </el-col>
                            </el-row>
                           <!-- <div class="idle-time">{{idle.timeStr}}</div>-->
                            <div class="user-info">
                                <el-image
                                    style="width: 30px; height: 30px"
                                    :src="(idle.user && idle.user.avatar) || defaultAvatar"
                                    fit="contain">
                                    <div slot="error" class="image-slot">
                                        <i class="el-icon-picture-outline">无图</i>
                                    </div>
                                </el-image>
                                <div class="user-nickname">{{ (idle.user && idle.user.nickname) || '匿名用户' }}</div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <div class="fenye">
                <el-pagination
                        background
                        @current-change="handleCurrentChange"
                        :current-page.sync="currentPage"
                        :page-size="8"
                        layout="prev, pager, next, jumper"
                        :total="totalItem">
                </el-pagination>
            </div>
            </div>
            <app-foot></app-foot>
        </app-body>
    </div>
</template>

<script>
    import AppHead from '../common/AppHeader.vue';
    import AppBody from '../common/AppPageBody.vue'
    import AppFoot from '../common/AppFoot.vue'

    export default {
        name: "index",
        components: { AppHead, AppBody, AppFoot },
        data() {
            return {
                labelName: '0',         // el-tabs 的 name 是字符串，保持字符串
                idleList: [],
                currentPage: 1,
                totalItem: 1,
                defaultAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
            };
        },
        created() {
            this.syncFromRoute();
            this.findIdleTiem(this.currentPage);
        },
        watch: {
            '$route.query': {
                handler() {
                    this.syncFromRoute();
                    this.findIdleTiem(this.currentPage);
                },
                deep: true
            }
        },
        methods: {
            // 读取路由并做类型归一
            syncFromRoute() {
                this.currentPage = Number(this.$route.query.page || 1);
                // 路由里没带 labelName 时，回到“全部”
                this.labelName = (this.$route.query.labelName !== undefined)
                    ? String(this.$route.query.labelName)
                    : '0';
            },

            findIdleTiem: function (page) {
                var loading = this.$loading({
                    lock: true, text: '加载数据中', spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)'
                });

                var labelNum = Number(this.labelName || 0);
                var isAll = !(isFinite(labelNum) && labelNum > 0);

                var api = isAll ? this.$api.findIdleTiem : this.$api.findIdleTiemByLable;
                var params = isAll ? { page: page, nums: 8 } : { idleLabel: labelNum, page: page, nums: 8 };

                var _this = this;
                api(params).then(function (res) {
                    // 兼容不同封装：有的拦截器返回的是 resp，有的是 resp.data
                    var data = (res && res.data) ? res.data : res;
                    var list = (data && Array.isArray(data.list)) ? data.list : [];

                    _this.idleList = list.map(function (it) {
                        var pictures = [];
                        try {
                            pictures = JSON.parse(it.pictureList || '[]');
                        } catch (e) {}
                        var timeStr = it.releaseTime
                            ? it.releaseTime.substring(0, 10) + " " + it.releaseTime.substring(11, 19)
                            : '';

                        // 不用展开语法，避免转译问题
                        var item = Object.assign({}, it);
                        item.timeStr = timeStr;
                        item.imgUrl = pictures[0] || '';
                        item.user = it.user || {}; // 兜底，避免模板空指针
                        return item;
                    });

                    _this.totalItem = Number((data && data.count) || 0);
                }).catch(function (e) {
                    console.log(e);
                    _this.idleList = [];
                    _this.totalItem = 0;
                }).finally(function () {
                    loading.close();
                });
            },


            // 切换标签：不要把“全部”写成 labelName=0
            handleClick() {
                const q = { page: 1 };
                if (Number(this.labelName) > 0) q.labelName = this.labelName;
                this.$router.replace({ query: q });
            },

            handleCurrentChange(val) {
                const q = { page: val };
                if (Number(this.labelName) > 0) q.labelName = this.labelName;
                this.$router.replace({ query: q });
            },

            toDetails(idle) {
                this.$router.push({ path: '/details', query: { id: idle.id } });
            }
        }
    }
</script>

<style scoped>
    .idle-card {
        height: 300px;
        border: #eeeeee solid 1px;
        margin-bottom: 15px;
        cursor: pointer;
    }

    .fenye {
        display: flex;
        justify-content: center;
        height: 60px;
        align-items: center;
    }

    .idle-title {
        font-size: 18px;
        font-weight: 600;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        margin: 10px;
    }

    .idle-prive {
        font-size: 16px;
        color: red;
    }

    .idle-place {
        font-size: 13px;
        color: #666666;
        float: right;
        padding-right: 20px;

    }

    .idle-time {
        color: #666666;
        font-size: 12px;
        margin: 0 10px;
    }

    .user-nickname {
        color: #999999;
        font-size: 12px;
        display: flex;
        align-items: center;
        height: 30px;
        padding-left: 10px;
    }

    .user-info {
        margin-top: 10px;
        float: right;
        padding: 5px 10px;
        height: 30px;
        display: flex;
    }
</style>