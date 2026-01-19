import OSS from 'ali-oss'



function getClient() {
  const region = import.meta.env.VITE_OSS_REGION
  const bucket = import.meta.env.VITE_OSS_BUCKET
  const accessKeyId = import.meta.env.VITE_OSS_ACCESS_KEY_ID
  const accessKeySecret = import.meta.env.VITE_OSS_ACCESS_KEY_SECRET
  if (!region || !bucket || !accessKeyId || !accessKeySecret) {
    throw new Error('请在环境变量中配置 OSS 相关信息')
  }
  // secure: true 通过 https 访问，避免浏览器阻止非安全请求
  return new OSS({ region, bucket, accessKeyId, accessKeySecret, secure: true })
}

export async function uploadToOSS(file, keyPrefix = '') {
  const client = getClient()
  const ext = file.name?.split('.').pop() || 'bin'
  const filename = `${Date.now()}-${Math.random().toString(16).slice(2)}.${ext}`
  const prefix = (import.meta.env.VITE_OSS_PREFIX || '').replace(/\/+$/,'')
  const nested = keyPrefix ? keyPrefix.replace(/\/+$/,'') : ''
  const objectKey = [prefix, nested, filename].filter(Boolean).join('/').replace(/\/+/, '/')
  const res = await client.put(objectKey, file)
  return res.url
}
