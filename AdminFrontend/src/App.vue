<script setup>
// Root simply renders routes; full class makes it fill the viewport
</script>

<template>
  <div id="app" class="full">
    <router-view />
  </div>
</template>

<style scoped>
#app.full {
  width: 100%;
  min-height: 100vh;
}
</style>
