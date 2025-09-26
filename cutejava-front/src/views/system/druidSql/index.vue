<template>
  <elFrame :src="druidSqlApi" />
</template>
<script>
import { mapGetters } from 'vuex'
import elFrame from '@/components/Iframe/index'

export default {
  name: 'DruidSqlConsole',
  components: { elFrame },
  computed: {
    ...mapGetters([
      'druidSqlApi'
    ])
  }
}
</script>
